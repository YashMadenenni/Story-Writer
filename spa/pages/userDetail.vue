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
            <v-text-field label="UserName" v-model="userName"></v-text-field>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-text-field label="Password" v-model="password" type="password"></v-text-field>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-btn color="primary"
                   :disabled="!this.userName || !this.role" @click="updateUser()">
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
  layout: "index",
  data() {
    return {
      role: '',
      roles: ["admin", "user"],
      userName: '',
      userEmail: '',
      password: '',
      error: "",
    }
  },
  mounted() {
    axios.get('/user', {
      params: {
        "userEmail": this.$route.query.userEmail,
      },
    }).then((response) => {
      this.userName = response.data.userName
      this.userEmail = response.data.userEmail
      this.password = response.data.password
      this.role = response.data.role
      console.log("response.data: ", response.data)
    }).catch((error) => {
      console.log('There is error:' + error.response)
    })
  },
  methods: {
    async updateUser() {
      await axios.put('/user', {
        "userName": this.userName,
        "roleChange": this.role,
        "currentRole": this.$route.query.role,
      }).then((response) => {
        console.log("response.data: ", response.data)
        this.$router.go(-1)
      }).catch((error) => {
        console.log('There is error:' + error.response)
      })
    },
    async deleteUser() {
      await axios.delete('/user', {
        params: {
          "userName": this.userEmail,
          "role": this.role,
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
