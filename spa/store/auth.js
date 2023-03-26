
export const state = () => ({
  loggedIn: false,
  role: ""
});

export const mutations = {
  login (state, payload) {
    console.log("payload"+payload)
    state.loggedIn = true
    state.role = payload
  },
  logout (state) {
    state.loggedIn = false
    state.role = ""
  },
};

export const getters = {
  loggedIn(state) {
    return state.loggedIn;
  },
  role(state) {
    return state.role;
  }
};
