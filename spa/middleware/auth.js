export default function ({ redirect, store, route}) {
  console.log("store.getters:"+store.getters["auth/loggedIn"])
  // if (!store.getters["auth/loggedIn"] && route.path !== '/') {
  //   redirect('/');
  // }
}
