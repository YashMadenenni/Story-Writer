<template>
  <v-container>
    <v-card>
      <v-card-title>
        Readable Page List
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
        {text: 'Title', value: 'title'},
      ],
      userEmail: '',
      role: '',
      pages: [{
        title: ""
      }],
      page: "",
      error: "",
    }
  },
  mounted() {
    this.userEmail = this.$store.getters["auth/userEmail"]
    this.role = this.$store.getters["auth/role"]
    axios.get("/page/access/my", {
      params: {
        "userEmail": this.userEmail,
      },
    }).then((response) => {
      for (var i in response.data) {
        this.pages[i].title = response.data[i];
      }
    }).catch((error) => {
    })
  },
  methods: {
    handleClick(row) {
      this.$router.push({
        path: '/readOnlyPageDetail', query: {
          userEmail: this.userEmail,
          title: row.title,
        }
      });
    },
  },
}
</script>
