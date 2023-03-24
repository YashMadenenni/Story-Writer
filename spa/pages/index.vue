<template>
  <v-app>
    <v-form>
      <v-container>
        <v-card>
          <v-card-title>
            Login
            <v-spacer />
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
                   :disabled="!this.userName || !this.password || !this.role"
                   @click="login()">
              Login
            </v-btn>
            <v-btn color="primary" @click="goRegisterUser()">
              Register User
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
      password: '',
    }
  },

  methods: {
    login() {
      this.$router.push('/createPage')
    },
    goRegisterUser() {
      this.$router.push('/registerUser')
    },
    // async login(e) {
    //   if (this.role == "admin") {
    //     await axios.post('/adminLogin', {
    //       "userName": this.userName,
    //       "password": this.password,
    //     }).then((response) => {
    //       console.log("response.data: ", response.data)
    //     }).catch((error) => {
    //       console.log('There is error:' + error.response)
    //     })
    //   } else {
    //     await axios.post('/userLogin', {
    //       "userName": this.userName,
    //       "password": this.password,
    //     }).then((response) => {
    //       console.log("response.data: ", response.data)
    //     }).catch((error) => {
    //       console.log('There is error:' + error.response)
    //     })
    //   }
    // }
  },
}

</script>
