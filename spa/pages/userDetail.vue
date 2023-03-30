<template>
  <v-app>
    <v-form>
      <v-container>
        <v-card>
          <v-card-title>
            User Detail
            <v-spacer/>
          </v-card-title>
        </v-card>
        <v-row>
          <v-col>
            <v-select v-model="role" :items="roles" label="Roles"></v-select>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-text-field :readonly=true label="UserEmail" v-model="userEmail"></v-text-field>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-btn color="primary"
                   :disabled="!this.role" @click="updateUser()">
              Update
            </v-btn>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-btn color="primary" @click="deleteUser()">
              Delete
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
      role: '',
      roles: ["admin", "user"],
      userName: '',
      userEmail: '',
      password: '',
      currentRole: "",
    }
  },
  mounted() {
    this.userEmail = this.$route.query.userEmail
    axios.get('/user', {
      params: {
        "userEmail": this.userEmail,
      },
    }).then((response) => {
      this.userName = response.data.userName
      this.userEmail = response.data.userEmail
      if (response.data.role == "StandardUser") {
        this.currentRole = "user"
      } else {
        this.currentRole = "admin"
      }
    }).catch((error) => {
      this.$toast.error(`Failed to load user error: ${error}`, {
        position: "top-center"
      })
    })
  },
  methods: {
    async updateUser() {
      await axios.post('/user/update', {
        "userEmail": this.userEmail,
        "currentRole": this.currentRole,
      }).then((response) => {
        this.$toast.success("Success Update User", {
          position: "top-center"
        })
        this.$router.go(-1)
      }).catch((error) => {
        this.$toast.error(`Failed to update user error: ${error}`, {
          position: "top-center"
        })
      })
    },
    async deleteUser() {
      await axios.delete('/user', {
        params: {
          "userEmail": this.userEmail,
        },
      }).then((response) => {
        this.$toast.success("Success Delete User", {
          position: "top-center"
        })
        this.$router.go(-1)
      }).catch((error) => {
        this.$toast.error(`Failed to delete user error: ${error}`, {
          position: "top-center"
        })
      })
    }
  },
}

</script>
