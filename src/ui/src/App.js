import './App.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import Layout from "./pages/Layout";
import Home from "./pages/Home";
import Servers from "./pages/Servers";
import CreateNewAccount from "./components/Login/CreateNewAccount";
import useToken from "./components/useToken";

function App() {
  const { token, setToken } = useToken();
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path={'/'} element={<Layout/>}>
            <Route index element={<Home/>}/>
            <Route path={'servers'} element={<Servers/>}/>
            <Route path={'createNewAccount'} element={<CreateNewAccount setToken={setToken}/>}/>
          </Route>
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
