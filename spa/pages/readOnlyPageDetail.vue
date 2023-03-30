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
            <v-text-field :readonly="true" label="Title" v-model="title"></v-text-field>
          </v-col>
        </v-row>
        <v-row>
          <v-textarea :readonly="true" v-model="content" outlined></v-textarea>
        </v-row>
      </v-container>
    </v-form>
  </v-app>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      userEmail: '',
      title: '',
      content: '',
      error: "",
    }
  },
  mounted() {
    this.userEmail = this.$route.query.userEmail
    axios.get('/page/info', {
      params: {
        "title": this.$route.query.title,
      },
    }).then((response) => {
      this.title = response.data.title
      this.content = response.data.content
    }).catch((error) => {
      this.$toast.error(`Failed to load page error: ${error}`, {
        position: "top-center"
      })
    })
  },
}

</script>
