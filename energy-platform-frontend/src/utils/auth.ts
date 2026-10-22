export const isAuthenticated = () => sessionStorage.getItem('authenticated') === 'true'
export const markAuthenticated = () => sessionStorage.setItem('authenticated', 'true')
export const clearAuthentication = () => sessionStorage.removeItem('authenticated')
