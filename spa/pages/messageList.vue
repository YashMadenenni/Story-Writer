<template>
  <v-container>
    <v-card>
      <v-card-title>
        Message List
        <v-spacer/>
      </v-card-title>
    </v-card>
    <v-row>
      <v-col>
        <v-card>
          <v-data-table
            :headers="headers"
            :items="messages"
            :items-per-page="10"
            sort-by="id"
            :sort-desc="true"
            class="elevation-1"
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
        {text: 'admin', value: 'admin'},
        {text: 'Message', value: 'message'},
      ],
      admin: "",
      message: "",
      messages: [],
      error: "",
    }
  },
  mounted() {
    axios.get('/page/admin/message')
      .then((response) => {
        console.log("response.data: ", response.data.message)
        this.messages = response.data.message
      }).catch((error) => {
      console.log('There is error:' + error.response)
    })
  },
}
</script>
