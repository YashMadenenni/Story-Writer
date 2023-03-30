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
          >
            <template v-slot:body="{ items: pages }">
              <tbody class="custom-body">
              <tr v-for="page in pages">
                <td>{{ page }}</td>
              </tr>
              </tbody>
            </template>
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
        // {text: 'Admin', value: 'admin'},
        {text: 'Title', value: 'pages'},
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
    axios.get("/page/access/my", {
      params: {
        "userEmail": this.userEmail,
      },
    }).then((response) => {
      console.log("response.data:"+response.data)
      this.pages = response.data
    }).catch((error) => {
    })
  },
}
</script>
