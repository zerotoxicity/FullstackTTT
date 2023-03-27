import { Button, Flex, Spacer } from '@chakra-ui/react';
import { useNavigate } from 'react-router-dom';
import {
  BACKEND_URL,
  errorAudio,
  JOIN_GAME_BODY,
  NEW_GAME_BODY,
} from '../constants';
import { checkJson } from '../helperFunctions';

const FormComponentButtons = ({ gameId, playerName, setError }) => {
  const navigate = useNavigate();

  // API request based on which button is pressed
  const fetchFunction = async join => {
    const reqBody = join ? JOIN_GAME_BODY : NEW_GAME_BODY;
    const id = join ? gameId + '/' : '';
    const url = BACKEND_URL;

    await fetch(url + 'games/' + id + playerName, reqBody)
      .then(async response => {
        const isJson = checkJson(response);
        let error = response;
        const data = isJson && (await response.json());
        if (!response.ok) {
          return Promise.reject(error);
        }
        // Switch to game board if the inputs are valid
        navigate('/game', { state: { data: data, player: playerName } });
      })
      .catch(error => {
        errorAudio.play();
        setError(true);
      });
  };

  const onNewGameClick = async () => {
    await fetchFunction(false);
  };

  const onJoinGameClick = async () => {
    await fetchFunction(true);
  };

  return (
    <Flex>
      <Button
        size={{ base: 'sm', md: 'lg' }}
        colorScheme={'gray'}
        isDisabled={!(gameId && playerName)}
        onClick={async () => {
          await onJoinGameClick();
        }}
      >
        Join Game
      </Button>

      <Spacer />

      <Button
        size={{ base: 'sm', md: 'lg' }}
        colorScheme={'green'}
        isDisabled={!playerName}
        onClick={async () => {
          await onNewGameClick();
        }}
      >
        New Game
      </Button>
    </Flex>
  );
};

export default FormComponentButtons;
