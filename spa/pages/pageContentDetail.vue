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
          <v-textarea v-model="content" outlined></v-textarea>
        </v-row>
        <v-row>
          <v-col>
            <v-btn color="primary"
                   :disabled="!this.title || !this.content" @click="updatePage()">
              Update
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
  data() {
    return {
      userEmail: '',
      title: '',
      content: '',
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
  methods: {
    async updatePage() {
      await axios.post('/page/content', {
        "userEmail": this.userEmail,
        "content": this.content,
        "pageName": this.title,
      }).then((response) => {
        this.$toast.success("Success Update Page", {
          position: "top-center"
        })
        this.$router.go(-1)
      }).catch((error) => {
        this.$toast.error(`Failed to update page: ${error}`, {
          position: "top-center"
        })
      })
    },
  },
}

</script>
