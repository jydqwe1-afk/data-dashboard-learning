const state = {
  token: localStorage.getItem('token') || '',
  userInfo: JSON.parse(localStorage.getItem('userInfo') || '{}'),
  role: localStorage.getItem('role') || ''
}

const getters = {
  isLoggedIn: state => !!state.token,
  isAdmin: state => state.role === 'admin',
  isDeptAdmin: state => state.role === 'dept_admin',
  currentUser: state => state.userInfo
}

const mutations = {
  SET_TOKEN(state, token) {
    state.token = token
    localStorage.setItem('token', token)
  },
  SET_USER_INFO(state, userInfo) {
    state.userInfo = userInfo
    localStorage.setItem('userInfo', JSON.stringify(userInfo))
  },
  SET_ROLE(state, role) {
    state.role = role
    localStorage.setItem('role', role)
  },
  LOGOUT(state) {
    state.token = ''
    state.userInfo = {}
    state.role = ''
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    localStorage.removeItem('role')
  }
}

const actions = {
  login({ commit }, { token, userInfo, role }) {
    commit('SET_TOKEN', token)
    commit('SET_USER_INFO', userInfo)
    commit('SET_ROLE', role)
  },
  logout({ commit }) {
    commit('LOGOUT')
  }
}

export default { namespaced: true, state, getters, mutations, actions }
