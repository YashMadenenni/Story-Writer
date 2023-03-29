<template>
  <v-container>
    <v-card>
      <v-card-title>
        Page List
        <v-spacer/>
      </v-card-title>
    </v-card>
    <v-row>
      <v-col>
        <v-card v-if="pages.length > 0">
          <v-data-table
            :headers="headers"
            :items="pages"
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
        {text: 'Admin', value: 'admin'},
        {text: 'Title', value: 'title'},
      ],
      userEmail: '',
      role: '',
      pages: [],
      page: "",
      error: "",
    }
  },
  mounted() {
    this.userEmail = this.$store.getters["auth/userEmail"]
    this.role = this.$store.getters["auth/role"]
    var endpoint = this.role === "admin" ? "page/admin" : "page"
    axios.get(`/${endpoint}`, {
      params: {
        "userEmail": this.userEmail,
      },
    }).then((response) => {
      console.log("response.data: ", response.data)
      this.pages = response.data
    })
    .catch((error) => {
      console.log('There is error:' + error.response)
    })
  },
  methods: {
    handleClick(row) {
      this.$router.push({
        path: '/pageDetail', query: {
          userEmail: this.userEmail,
          title: row.title,
          content: row.content,
          editAccessUser: row.editAccessUser,
        }
      });
    },
  },
}
</script>
