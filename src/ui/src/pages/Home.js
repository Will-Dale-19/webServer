import React from 'react';
import { Link } from 'react-router-dom'
import Login from "../components/Login/Login";
import useToken from "../components/useToken";

const Home = () => {
    const { token, setToken } = useToken();

    if(!token) {
        return (
            <div>
                <Login setToken={setToken} />
                <h3>No account? Create one here!</h3>
                <div>
                    <button type="submit" onClick={gotoCreateAccount}>Create new account</button>
                </div>
            </div>

        )
    }

  return (
    <>
      <h3>Home</h3>
      <ul style={{listStyleType: 'none'}}>
        <li><Link to={'/servers'}>Servers</Link></li>
      </ul>
        <div>
            <h3>Logout</h3>
            <button type="submit" onClick={logout}>logout</button>
        </div>
    </>
  )
};
function logout() {
    sessionStorage.clear();
    window.location.href="http://localhost:3000"
}
function gotoCreateAccount() {
    window.location.href="http://localhost:3000/createNewAccount";
}

export default Home;