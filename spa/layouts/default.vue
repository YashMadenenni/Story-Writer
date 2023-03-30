<template>
  <v-app dark>
    <v-navigation-drawer
      v-model="drawer"
      :mini-variant="miniVariant"
      :clipped="clipped"
      fixed
      app
    >
      <v-list>
        <v-list-item
          v-for="(item, i) in items"
          :key="i"
          :to="item.to"
          router
          exact
        >
          <v-list-item-action>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
    <v-app-bar
      :clipped-left="clipped"
      fixed
      app
    >
      <v-app-bar-nav-icon @click.stop="drawer = !drawer" />
      <v-btn
        icon
        @click.stop="miniVariant = !miniVariant"
      >
        <v-icon>mdi-{{ `chevron-${miniVariant ? 'right' : 'left'}` }}</v-icon>
      </v-btn>
      <v-btn
        icon
        @click.stop="clipped = !clipped"
      >
        <v-icon>mdi-application</v-icon>
      </v-btn>
      <v-btn
        icon
        @click.stop="fixed = !fixed"
      >
        <v-icon>mdi-minus</v-icon>
      </v-btn>
      <v-toolbar-title>{{ title }}</v-toolbar-title>
      <v-btn color="primary" @click="logout()" style="margin-left: 10px">Logout</v-btn>
      <v-spacer />
      <v-btn
        icon
        @click.stop="rightDrawer = !rightDrawer"
      >
        <v-icon>mdi-menu</v-icon>
      </v-btn>
    </v-app-bar>
    <v-main>
      <v-container>
        <Nuxt />
      </v-container>
    </v-main>
    <v-navigation-drawer
      v-model="rightDrawer"
      :right="right"
      temporary
      fixed
    >
      <v-list>
        <v-list-item @click.native="right = !right">
          <v-list-item-action>
            <v-icon light>
              mdi-repeat
            </v-icon>
          </v-list-item-action>
          <v-list-item-title>Switch drawer (click me)</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
    <v-footer
      :absolute="!fixed"
      app
    >
      <span>&copy; {{ new Date().getFullYear() }}</span>
    </v-footer>
  </v-app>
</template>

<script>
import axios from "axios";

export default {
  name: 'DefaultLayout',
  mounted() {
    if (this.$store.getters["auth/role"] == "admin") {
      this.items = [
        {
          icon: 'mdi-apps',
          title: 'Create Page',
          to: '/createPage'
        },
        {
          icon: 'mdi-chart-bubble',
          title: 'Page List',
          to: '/pageList'
        },
        {
          icon: 'mdi-chart-bubble',
          title: 'User List',
          to: '/userList'
        },
        {
          icon: 'mdi-chart-bubble',
          title: 'Create Message',
          to: '/createMessage'
        },
        {
          icon: 'mdi-chart-bubble',
          title: 'Message List',
          to: '/messageList'
        },
      ]
    } else {
      this.items = [
        {
          icon: 'mdi-apps',
          title: 'Create Page',
          to: '/createPage'
        },
        {
          icon: 'mdi-chart-bubble',
          title: 'Page List',
          to: '/userPageList'
        },
        {
          icon: 'mdi-chart-bubble',
          title: 'Message List',
          to: '/messageList'
        },
      ]
    }
  },
  data () {
    return {
      clipped: false,
      drawer: false,
      fixed: false,
      items: [],
      miniVariant: false,
      right: true,
      rightDrawer: false,
      title: 'Vuetify.js'
    }
  },
  methods: {
    logout() {
      this.$store.commit("auth/logout")
      this.$router.push('/');
    },
  }
}
</script>
