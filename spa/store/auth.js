
export const state = () => ({
  loggedIn: false,
  role: "",
  userEmail: ""
});

export const mutations = {
  login (state, payload) {
    state.loggedIn = true
    state.role = payload
  },
  logout (state) {
    state.loggedIn = false
    state.role = ""
    state.userEmail = ""
  },
  setEmail (state, payload) {
    state.userEmail = payload
  },
};

export const getters = {
  loggedIn(state) {
    return state.loggedIn;
  },
  role(state) {
    return state.role;
  },
  userEmail(state) {
    return state.userEmail;
  }
};
