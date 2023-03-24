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
        {text: 'Title', value: 'title'},
        {text: 'Content', value: 'content'},
      ],
      pages: [
        {
          title: "hoge1",
          content: "hogehogehogehogehogehogehogehoge",
        },
        {
          title: "hoge2",
          content: "hogehogehogehogehogehogehogehoge",
        }
      ],
      page: "",
      error: "",
    }
  },
  mounted() {
    // axios.get('/pages')
    // .then((response) => {
    //   console.log("response.data: ", response.data)
    //   this.pages = response.data
    // })
    // .catch((error) => {
    //   console.log('There is error:' + error.response)
    // })
  },
  methods: {
    handleClick(row) {
      this.$router.push({
        path: '/pageDetail', query: {
          title: row.title,
          content: row.content,
        }
      });
    },
  },
}
</script>
