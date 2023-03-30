<template>
  <v-app>
    <v-form>
      <v-container>
        <v-card>
          <v-card-title>
            Page Detail
            <v-spacer/>
          </v-card-title>
        </v-card>
        <v-row>
          <v-col>
            <v-text-field :readonly="true" label="Title" v-model="title"></v-text-field>
          </v-col>
        </v-row>
        <v-row>
          <v-textarea v-model="content" outlined></v-textarea>
        </v-row>
        <v-row>
          <v-col>
            <v-btn color="primary"
                   :disabled="!this.title || !this.content" @click="updatePage()">
              Update
            </v-btn>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-select v-model="readAccessUser" :items="users" label="Read Access Users"></v-select>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-btn color="primary"
                   :disabled="!this.readAccessUser" @click="addReadAccessUser()">
              Add read access user
            </v-btn>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-btn color="primary"
                   :disabled="!this.readAccessUser" @click="deleteReadAccessUser()">
              Delete read access user
            </v-btn>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-select v-model="editAccessUser" :items="users" label="Edit Access Users"></v-select>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-btn color="primary"
                   :disabled="!this.editAccessUser" @click="addEditAccessUser()">
              Add edit access user
            </v-btn>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-btn color="primary"
                   :disabled="!this.editAccessUser" @click="deleteEditAccessUser()">
              Delete edit access user
            </v-btn>
          </v-col>
        </v-row>
      </v-container>
    </v-form>
  </v-app>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      userEmail: '',
      title: '',
      content: '',
      users: [],
      readAccessUser: '',
      editAccessUser: '',
    }
  },
  mounted() {
    this.userEmail = this.$route.query.userEmail
    axios.get('/page/info', {
      params: {
        "title": this.$route.query.title,
      },
    }).then((response) => {
      this.title = response.data.title
      this.content = response.data.content
    }).catch((error) => {
      this.$toast.error(`Failed to load page error: ${error}`, {
        position: "top-center"
      })
    }),
    axios.get('/users')
      .then((response) => {
        for (var i in response.data.users) {
          this.users.push(response.data.users[i].userEmail);
        }
      }).catch((error) => {
      this.$toast.error(`Failed to load page error: ${error}`, {
        position: "top-center"
      })
    })
  },
  methods: {
    async updatePage() {
      await axios.post('/page/content', {
        "userEmail": this.userEmail,
        "content": this.content,
        "pageName": this.title,
      }).then((response) => {
        this.$toast.success("Success Update Page", {
          position: "top-center"
        })
        this.$router.go(-1)
      }).catch((error) => {
        this.$toast.error(`Failed to update page: ${error}`, {
          position: "top-center"
        })
      })
    },
    async addReadAccessUser() {
      await axios.post('/page/access/user', {
        "userEmail": this.readAccessUser,
        "pageName": this.title,
      }).then((response) => {
        this.$toast.success("Success add user to the page", {
          position: "top-center"
        })
        this.$router.go(-1)
      }).catch((error) => {
        this.$toast.error(`Failed to add user to the page error: ${error}`, {
          position: "top-center"
        })
      })
    },
    async deleteReadAccessUser() {
      await axios.delete('/page/access/user/gui', {
        params: {
          "userEmail": this.readAccessUser,
          "pageName": this.title,
        },
      }).then((response) => {
        this.$toast.success("Success delete user from the page", {
          position: "top-center"
        })
        this.$router.go(-1)
      }).catch((error) => {
        this.$toast.error(`Failed to delete user from the page error: ${error}`, {
          position: "top-center"
        })
      })
    },
    async addEditAccessUser() {
      await axios.post('/page/access', {
        "userEmail": this.editAccessUser,
        "pageName": this.title,
      }).then((response) => {
        this.$toast.success("Success add user to the page", {
          position: "top-center"
        })
        this.$router.go(-1)
      }).catch((error) => {
        this.$toast.error(`Failed to add user to the page error: ${error}`, {
          position: "top-center"
        })
      })
    },
    async deleteEditAccessUser() {
      await axios.delete('/page/access/gui', {
        params: {
          "userEmail": this.editAccessUser,
          "pageName": this.title,
        },
      }).then((response) => {
        this.$toast.success("Success delete user from the page", {
          position: "top-center"
        })
        this.$router.go(-1)
      }).catch((error) => {
        this.$toast.error(`Failed to delete user from the page error: ${error}`, {
          position: "top-center"
        })
      })
    },
  },
}

</script>
