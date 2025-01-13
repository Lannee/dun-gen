export function getToken() {
    let token = localStorage.getItem('token');
    return token;
}

export function setTokenLS(userToken) {
    localStorage.setItem('token', userToken);
}

export function logout(navigate) {
    localStorage.removeItem("token");
    navigate("/home", {replace: true})
    navigate(0)
}
