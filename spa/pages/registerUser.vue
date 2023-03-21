<template>
  <v-app>
    <v-form>
      <v-container>
        <v-card>
          <v-card-title>
            Register Usr
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
                   :disabled="!this.userName || !this.password || !this.role"
                   @click="register()">
              Submit
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
    // register() {
    //   this.$router.go(-1)
    // },
    async register(e) {
      await axios.post('/user', {
        "userName": this.userName,
        "password": this.password,
        "role": this.role == "admin" ? 1 : 2,
      }).then((response) => {
        console.log("response.data: ", response.data)
      }).catch((error) => {
        console.log('There is error:' + error.response)
      })
    }
  },
}

</script>
