import React, {useState} from 'react';
import PropTypes from 'prop-types';

async function loginUser(credentials) {
    return fetch(`http://localhost:8080/api/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(credentials)
    })
        .then(checkError)
        .then(data => data.json())
}

const checkError = (response) => {
    if(response.status !== 200){
        showApiError();
    }
    return response;
}

function showApiError() {
    throw new Error("Api Error");
}

function Login({setToken}) {
    const [username, setUserName] = useState();
    const [password, setPassword] = useState();

    const handleSubmit = async e => {
        e.preventDefault();
        try {
            const token = await loginUser({
                username,
                password
            });
            setToken(token);
        }
        catch(ex){
            alert("invalid login")
        }
    }

    return(
        <div className="login-wrapper">
            <h1>Please Log In Here</h1>
            <form onSubmit = {handleSubmit}>
                <label>
                    <p>Username</p>
                    <input type="text" onChange={e=> setUserName(e.target.value)}/>
                </label>
                <label>
                    <p>Password</p>
                    <input type="password" onChange={e => setPassword(e.target.value)}/>
                </label>
                <div>
                    <button type="submit">Submit</button>
                </div>
            </form>
        </div>
    )
}

Login.propTypes = {
    setToken: PropTypes.func.isRequired
}

export default Login;