import React, {useState} from "react";

async function createAccount(credentials) {
    return fetch(`http://localhost:8080/api/createAccount`, {
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

const CreateNewAccount = ({setToken}) => {
    const [username, setUserName] = useState();
    const [password, setPassword] = useState();

    const handleSubmit = async e => {
        e.preventDefault();
        try {
            const token = await createAccount({
                username,
                password
            });
            setToken(token);
        }
        catch(ex){
            alert("invalid login")
        }
    }

    return (
        <div className="create-account-wrapper">
            <h1>Create your account</h1>
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
                    <button type="submit" onClick={goBackHome}>Create Account</button>
                </div>
            </form>
        </div>
    )
}

function goBackHome(){
    window.location.href="http://localhost:3000";
}

export default CreateNewAccount;