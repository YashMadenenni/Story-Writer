<template>
  <v-app>
    <v-form>
      <v-container>
        <v-card>
          <v-card-title>
            Page Detail
            <v-spacer/>
          </v-card-title>
        </v-card>
        <v-row>
          <v-col>
            <v-text-field label="Title" v-model="title"></v-text-field>
          </v-col>
        </v-row>
        <v-row>
          <v-textarea v-model="content" class="my-textarea" outlined></v-textarea>
        </v-row>
        <v-row>
          <v-col>
            <v-btn color="primary"
                   :disabled="!this.title || !this.content" @click="updatePage()">
              Update
            </v-btn>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-btn color="primary" @click="deletePage()">
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
  layout: "index",
  data() {
    return {
      title: '',
      content: '',
      error: "",
    }
  },
  mounted() {
    this.title = this.$route.query.title
    this.content = this.$route.query.content
    // axios.get('/page', {
    //   "title": this.title,
    // }).then((response) => {
    //   this.title = this.$route.query.title
    //   this.content = this.$route.query.content
    //   console.log("response.data: ", response.data)
    //   // this.error = response.data.length == 0 ? "No Bus Route found" : "";
    // }).catch((error) => {
    //   console.log('There is error:' + error.response)
    // })
  },
  methods: {
    async updatePage() {
      await axios.put('/page', {
        "title": this.title,
        "content": this.content,
      }).then((response) => {
        console.log("response.data: ", response.data)
        this.$router.go(-1)
      }).catch((error) => {
        console.log('There is error:' + error.response)
      })
    },
    async deletePage() {
      await axios.delete('/page', {
        "title": this.title,
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
