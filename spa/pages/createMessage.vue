<template>
  <v-app>
    <v-form>
      <v-container>
        <v-card>
          <v-card-title>
            Create Message
            <v-spacer/>
          </v-card-title>
        </v-card>
        <v-row>
          <v-col>
            <v-text-field label="Message" v-model="message"></v-text-field>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-btn color="primary"
                   :disabled="!this.message"
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
      message: '',
      error: "",
    }
  },
  mounted() {
    this.userEmail = this.$store.getters["auth/userEmail"]
  },
  methods: {
    async register(e) {
      await axios.post('/page/admin/message', {
        "userEmail": this.userEmail,
        "message": this.message,
      }).then((response) => {
        this.$toast.success("Success Create Message", {
          position: "top-center"
        })
      }).catch((error) => {
        this.$toast.error(`Failed to create message error: ${error}`, {
          position: "top-center"
        })
      })
    }
  },
}

</script>
