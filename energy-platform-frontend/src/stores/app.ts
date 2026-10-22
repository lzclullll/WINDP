import { defineStore } from 'pinia'
export const useAppStore = defineStore('app', { state: () => ({ dark: localStorage.getItem('theme') === 'dark' }), actions: { toggleTheme(){ this.dark=!this.dark; localStorage.setItem('theme', this.dark?'dark':'light') } } })
