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
<!--        <v-row>-->
<!--          <v-col>-->
<!--            <v-textarea placeholder="Content" v-model="content" outlined></v-textarea>-->
<!--          </v-col>-->
<!--        </v-row>-->
        <v-row>
          <v-col>
            <v-btn color="primary"
                   :disabled="!this.title"
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
      userEmail: '',
      title: '',
      // content: '',
      error: "",
    }
  },
  mounted() {
    this.userEmail = this.$store.getters["auth/userEmail"]
    console.log("loggedIn:"+this.$store.getters["auth/loggedIn"])
    console.log("role:"+this.$store.getters["auth/role"])
    console.log("userEmail:"+this.$store.getters["auth/userEmail"])
  },
  methods: {
    async register(e) {
      await axios.post('/page', {
        "userEmail": this.userEmail,
        "pageName": this.title,
        // "content": this.content,
      }).then((response) => {
        console.log("response.data: ", response.data)
      }).catch((error) => {
        console.log('There is error:' + error.response)
      })
    }
  },
}

</script>
