import React, { useLayoutEffect, useState } from 'react'
import Buttons from "../components/Buttons";

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
            <Buttons />
    </div>

    )

}

export default Servers