<template>
  <v-app>
    <v-form>
      <v-container>
        <v-card>
          <v-card-title>
            Create Page
            <v-spacer/>
          </v-card-title>
        </v-card>
        <v-row>
          <v-col>
            <v-text-field label="Title" v-model="title"></v-text-field>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-textarea placeholder="Content" v-model="content" outlined></v-textarea>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-btn color="primary"
                   :disabled="!this.title || !this.content"
                   @click="register()">
              Submit
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
  layout: "default",
  data() {
    return {
      title: '',
      content: '',
      error: "",
    }
  },
  mounted() {
    $nuxt.setLayout('default')
    // this.title = this.$route.query.title
    // this.content = this.$route.query.content
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
    async register(e) {
      await axios.post('/page', {
        "title": this.title,
        "content": this.content,
      }).then((response) => {
        console.log("response.data: ", response.data)
      }).catch((error) => {
        console.log('There is error:' + error.response)
      })
    }
  },
}

</script>
