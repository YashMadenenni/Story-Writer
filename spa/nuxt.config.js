import colors from 'vuetify/es5/util/colors'

export default {
  router: {
    middleware: ['auth'],
  },
  // Disable server-side rendering: https://go.nuxtjs.dev/ssr-mode
  ssr: false,

  // Global page headers: https://go.nuxtjs.dev/config-head
  head: {
    titleTemplate: '%s - spa',
    title: 'spa',
    htmlAttrs: {
      lang: 'en'
    },
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'description', name: 'description', content: '' },
      { name: 'format-detection', content: 'telephone=no' }
    ],
    link: [
      { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }
    ]
  },

  // Global CSS: https://go.nuxtjs.dev/config-css
  css: [
  ],

  // Plugins to run before rendering page: https://go.nuxtjs.dev/config-plugins
  plugins: [
  ],

  // Auto import components: https://go.nuxtjs.dev/config-components
  components: true,

  // Modules for dev and build (recommended): https://go.nuxtjs.dev/config-modules
  buildModules: [
    // https://go.nuxtjs.dev/typescript
    '@nuxt/typescript-build',
    // https://go.nuxtjs.dev/vuetify
    '@nuxtjs/vuetify',
    '@nuxtjs/axios',
    '@nuxtjs/proxy'
  ],

  // Modules: https://go.nuxtjs.dev/config-modules
  modules: [
  ],

  axios: {
    proxy: true,
  },
  proxy: {
    '/user': 'http://localhost:8080',
    '/users': 'http://localhost:8080',
    '/user/update': 'http://localhost:8080',
    '/admin': 'http://localhost:8080',
    '/page': 'http://localhost:8080',
    '/page/admin': 'http://localhost:8080',
    '/page/admin/message': 'http://localhost:8080',
    '/page/info': 'http://localhost:8080',
    '/page/content': 'http://localhost:8080',
    '/page/access': 'http://localhost:8080',
    '/page/access/user': 'http://localhost:8080',
    '/page/access/my': 'http://localhost:8080',
    '/page/access/mywrite': 'http://localhost:8080',
    '/userLogin': 'http://localhost:8080',
    '/adminLogin': 'http://localhost:8080',
  },

  // Vuetify module configuration: https://go.nuxtjs.dev/config-vuetify
  vuetify: {
    customVariables: ['~/assets/variables.scss'],
    theme: {
      dark: true,
      themes: {
        dark: {
          primary: colors.blue.darken2,
          accent: colors.grey.darken3,
          secondary: colors.amber.darken3,
          info: colors.teal.lighten1,
          warning: colors.amber.base,
          error: colors.deepOrange.accent4,
          success: colors.green.accent3
        }
      }
    }
  },

  // Build Configuration: https://go.nuxtjs.dev/config-build
  build: {
  }
}
