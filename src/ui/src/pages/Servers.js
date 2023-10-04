import React, { useLayoutEffect, useState } from 'react'
import styled from "styled-components";

const Servers = () => {
    const [servers, setServers] = useState([])

    useLayoutEffect(()=> {
        const getServers = async() => {
            const res = await fetch('/api/servers');
            const servers = await res.json();
            setServers(servers);
        }
        getServers().catch(e => {
            console.log('error fetching customers: ' + e);
        })
    })



    return (
        <div>
        <table>
            <thead>
            <th>ID</th>
            <th>Server Name</th>
            <th>Server Location</th>
            </thead>
            <tbody>
            {servers.map(server => {
                const {
                    serverId,
                    serverName,
                    serverLocation
                } = server;
                return(
                    <tr key={serverId}>
                        <td>{serverId}</td>
                        <td>{serverName}</td>
                        <td>{serverLocation}</td>
                    </tr>
                )
            })}
            </tbody>
        </table>
        <Button onClick={clickMe}>Button</Button>
    </div>

    )

}

const theme = {
    blue: {
        default: "#3f51b5",
        hover: "#283593"
    },
    pink: {
        default: "#e91e63",
        hover: "#ad1457"
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

function clickMe() {
    alert("You clicked me!");
}

export default Servers