<template>
  <v-container>
    <v-card>
      <v-card-title>
        User List
        <v-spacer />
      </v-card-title>
    </v-card>
    <v-row>
      <v-col>
        <v-card v-if="users.length > 0">
          <v-data-table
            :headers="headers"
            :items="users"
            :items-per-page="10"
            sort-by="id"
            :sort-desc="true"
            class="elevation-1"
            @click:row="handleClick"
          >
          </v-data-table>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      headers: [
        {text: 'User name', value: 'userName'},
        {text: 'Email', value: 'email'},
        {text: 'Role', value: 'role'},
      ],
      users: [
        {
          userName: "hoge1",
          email: "hoge1@email.com",
          role: "admin",
        },
        {
          userName: "hoge2",
          email: "hoge2@email.com",
          role: "user",
        }
      ],
      user: "",
      error: "",
    }
  },
  mounted() {
    // axios.get('/users')
    //   .then((response) => {
    //     console.log("response.data: ", response.data)
    //     this.users = response.data
    //     // this.daysOfOperation = response.data
    //   })
    //   .catch((error) => {
    //     console.log('There is error:' + error.response)
    //   })
  },
  methods: {
    handleClick(row) {
      console.log(row.userName)
    },
    async getUser() {
      await axios.get('/user', {
        "userName": this.userName,
      }).then((response) => {
        console.log("response.data: ", response.data)
        // this.error = response.data.length == 0 ? "No Bus Route found" : "";
      }).catch((error) => {
        console.log('There is error:' + error.response)
      })
    },
  },
}
</script>
