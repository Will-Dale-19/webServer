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

function ToggleGroup() {
    const [active, setActive] = useState(types[0]);

    return (
        <div>
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
                        if (type === types[0]) {
                            setActive(types[1])
                        } else {
                            setActive(types[0])
                        }
                        changeServerStatus(type)
                        }
                    }
                    disabled = { active !== type}
                >
                    {type}
                </ButtonToggle>
            ))}
        </div>
    );
}

function clickMe() {
    alert("You clicked me!");
}

function changeServerStatus(type) {
    alert(type)
}

const Buttons = () => {
    return (
        <div>
            <div><Button onClick={clickMe}>Button</Button></div>
            <div><ToggleGroup /></div>
        </div>
    )
}
export default Buttons