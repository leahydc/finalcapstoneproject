import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)

/*
 * The authorization header is set for axios when you login but what happens when you come back or
 * the page is refreshed. When that happens you need to check for the token in local storage and if it
 * exists you should set the header so that it will be attached to each request
 */
const currentToken = localStorage.getItem('token')
const currentUser = JSON.parse(localStorage.getItem('user'));

if(currentToken != null) {
  axios.defaults.headers.common['Authorization'] = `Bearer ${currentToken}`;
}

export default new Vuex.Store({
  state: {
    token: currentToken || '',
    user: currentUser || {}, 
    searchResults: [], 
    currentInviteId: '',
    currentInvite: {},
    resInvite: {},
    pendingInviteRestaurants: [],
    finalistRestaurants: [],
    currentUserId: '',


  },
  mutations: {
    SET_AUTH_TOKEN(state, token) {
      state.token = token;
      localStorage.setItem('token', token);
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
    },
    SET_USER(state, user) {
      state.user = user;
      localStorage.setItem('user',JSON.stringify(user));
    },
    LOGOUT(state) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      state.token = '';
      state.user = {};
      axios.defaults.headers.common = {};
    }, 
    SET_SEARCH_RESULTS(state, results){
      state.searchResults = results;
    }, 
    SET_CURRENT_INVITE(state, invite){
      state.currentInvite = invite;
    }, 
    SET_PENDING_RESTAURANTS(state, pendingList){
      state.finalistRestaurants = pendingList;
    },
    SET_FINALIST_RESTAURANTS(state, finalistList){
      state.finalistRestaurants = finalistList;
    },
    SET_CURRENT_INVITE_ID(state, inviteId){
      state.currentInviteId = inviteId;
    }

  }
})
