export default function ({ redirect, store, route}) {

  if (!store.getters["auth/loggedIn"] && route.path !== '/') {
    redirect('/');
  }
}
