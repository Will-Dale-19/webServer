import styled from "styled-components";
import React, {useState} from "react";

const theme = {
    blue: {
        default: "#3f51b5",
        hover: "#283593"
    }
};

const Button = styled.button`
  background-color: ${(props) => theme[props.theme].default};
  color: white;
  padding: 5px 15px;
  border-radius: 5px;
  outline: 0;
  text-transform: uppercase;
  margin: 10px 0px;
  cursor: pointer;
  box-shadow: 0px 2px 2px lightgray;
  transition: ease background-color 250ms;
  &:hover {
    background-color: ${(props) => theme[props.theme].hover};
  }
  &:disabled {
    cursor: default;
    opacity: 0.7;
  }
`;

Button.defaultProps = {
    theme: "blue"
};

const ButtonToggle = styled(Button)`
  opacity: 0.7;
  cursor: default;
  ${({ active }) =>
    active &&
    `
    cursor: pointer;
    opacity: 1; 
  `}
`;

const types = ["Start Server", "Stop Server"];

function ToggleGroup({server}) {
    const [active, setActive] = useState(types[0]);

    return (
        <ul>
            {types.map((type) => (
                // This makes it so that if you click "start server",
                // the active type is set to "stop server" to reflect
                // the server status.
                <ButtonToggle
                    id = {"Button"+type}
                    active={
                        active === type
                    }
                    onClick={()=> {
                        const response = active === types[0]  ? sendStartServerRequest(server) : sendStopServerRequest(server)
                        response.then((result) => {

                            console.log(result.status)
                            if (result.status !== "FAILED-TO-START"){
                                type === types[0] ? setActive(types[1]) : setActive(types[0]);
                            } else {
                                alert("Server failed to start!");
                            }
                        })

                        }
                    }
                    disabled = { active !== type}
                >
                    {type}
                </ButtonToggle>
            ))}
        </ul>
    );
}

async function sendServerUpdate(type, serverName) {
    return fetch(`http://localhost:8080/api/servers/sendServerStatusUpdate`, {
        method: 'POST',
        body: JSON.stringify({"type":type, "serverName":serverName})
    })
        .then(checkError)
        .then(data => data.json())
}
async function sendStartServerRequest(serverName) {

    return fetch(`http://localhost:8080/api/servers/startServer`, {
        method: 'POST',
        body: JSON.stringify(serverName)
    })
        .then(checkError)
        .then(data => data.json())
}

async function sendStopServerRequest(serverName) {

    return fetch(`http://localhost:8080/api/servers/stopServer`, {
        method: 'POST',
        body: JSON.stringify(serverName)
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

const Buttons = ({server}) => {
    return (
        <ToggleGroup server={server}/>
    )
}
export default Buttons