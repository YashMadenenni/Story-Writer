export default ({ store, redirect, route }) => {
  if (!store.state.loggedIn) {
    return redirect({
      path: '/',
      query: {
        showMessage: true,
        redirect: route.path
      }
    })
  }
}
