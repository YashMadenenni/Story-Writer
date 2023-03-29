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
            <v-text-field :readonly=true label="UserName" v-model="userName"></v-text-field>
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
      error: "",
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
      // this.role = response.data.role
      console.log("response.data: ", response.data)
    }).catch((error) => {
      console.log('There is error:' + error.response)
    })
  },
  methods: {
    async updateUser() {
      await axios.post('/user/update', {
        "userEmail": this.userEmail,
        "currentRole": this.role,
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
          "userEmail": this.userEmail,
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
