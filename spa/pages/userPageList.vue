<template>
  <v-container>
    <v-card>
      <v-card-title>
        Your Own Page List
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
    axios.get("/page", {
      params: {
        "userEmail": this.userEmail,
      },
    }).then((response) => {
      this.pages = response.data
    }).catch((error) => {
    })
  },
  methods: {
    handleClick(row) {
      this.$router.push({
        path: '/pageDetail', query: {
          userEmail: this.userEmail,
          title: row.title,
          content: row.content,
        }
      });
    },
  },
}
</script>
