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
            <v-text-field label="Title" v-model="title"></v-text-field>
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
            <v-btn color="primary" @click="deletePage()">
              Delete
            </v-btn>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-select v-model="user" :items="users" label="Read Access Users"></v-select>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-btn color="primary"
                   :disabled="!this.user" @click="addUser()">
              Add user to editAccessUser
            </v-btn>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-select v-model="editAccessUser" :items="editAccessUsers" label="Edit Access Users"></v-select>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-btn color="primary"
                   :disabled="!this.editAccessUser" @click="deleteUser()">
              Delete user from editAccessUser
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
      error: "",
      editAccessUsers: [],
      editAccessUser: '',
      users: [],
      user: '',
    }
  },
  mounted() {
    this.userEmail = this.$route.query.userEmail
    this.title = this.$route.query.title
    this.content = this.$route.query.content
    this.editAccessUsers = this.$route.query.editAccessUser
    axios.get('/users')
      .then((response) => {
        console.log("response.data: ", response.data.users)
        for (var i in response.data.users) {
          console.log("hoge1:"+response.data.users[i].userEmail)
          this.users.push(response.data.users[i].userEmail);
        }
      }).catch((error) => {
      console.log('There is error:' + error.response)
    })
  },
  methods: {
    async updatePage() {
      await axios.post('/page/content', {
        "userEmail": this.userEmail,
        "content": this.content,
        "pageName": this.title,
      }).then((response) => {
        console.log("response.data: ", response.data)
        this.$router.go(-1)
      }).catch((error) => {
        console.log('There is error:' + error.response)
      })
      // await axios.put('/page', null, {
      //   params: {
      //     "userEmail": this.userEmail,
      //     "content": this.content,
      //     "pageName": this.title,
      //   },
      // }).then((response) => {
      //   console.log("response.data: ", response.data)
      //   this.$router.go(-1)
      // }).catch((error) => {
      //   console.log('There is error:' + error.response)
      // })
    },
    async deletePage() {
      await axios.delete('/page', {
        "pageName": this.title,
      }).then((response) => {
        console.log("response.data: ", response.data)
        this.$router.go(-1)
      }).catch((error) => {
        console.log('There is error:' + error.response)
      })
    },
    async addUser() {
      await axios.post('/page/access', {
        "userEmail": this.user,
        "pageName": this.title,
      }).then((response) => {
        console.log("response.data: ", response.data)
        this.$router.go(-1)
      }).catch((error) => {
        console.log('There is error:' + error.response)
      })
    },
    async deleteUser() {
      await axios.delete('/page/access', {
        params: {
          "userEmail": this.editAccessUser,
          "pageName": this.title,
        },
      }).then((response) => {
        console.log("response.data: ", response.data)
        this.$router.go(-1)
      }).catch((error) => {
        console.log('There is error:' + error.response)
      })
    }
  },
}

</script>
