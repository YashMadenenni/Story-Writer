<template>
  <v-container>
    <v-card>
      <v-card-title>
        User List
        <v-spacer/>
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
        {text: 'Email', value: 'userEmail'},
      ],
      users: [],
      user: "",
      error: "",
    }
  },
  mounted() {
    axios.get('/users')
    .then((response) => {
      this.users = response.data.users
    }).catch((error) => {
    })
  },
  methods: {
    handleClick(row) {
      this.$router.push({
        path: '/userDetail', query: {
          userEmail: row.userEmail,
        }
      });
    },
  },
}
</script>
