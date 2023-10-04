import React, { useLayoutEffect, useState } from 'react'

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
    )

}

export default Servers