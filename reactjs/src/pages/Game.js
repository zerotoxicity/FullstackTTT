import { Grid, GridItem } from '@chakra-ui/react';
import { useState } from 'react';
import { useLocation } from 'react-router-dom';
import { BACKEND_URL } from '../constants';
import { useSubscription } from 'react-stomp-hooks';
import GameDetails from '../components/GameComponents/GameDetails';
import GameArea from '../components/GameComponents/GameArea';

const Game = () => {
  const [game, setGame] = useState();
  const [player, setPlayer] = useState();
  const location = useLocation().state;

  if (location && !game) {
    setGame(location.data);
    setPlayer(location.player);
  }
  //2@NEWPLAYER
  useSubscription(`/topic/lobby`, message => {
    const id = message.body.split('@')[0];
    if (game && game.gameId == id) {
      fetch(BACKEND_URL + 'games/' + id)
        .then(async response => await response.json())
        .then(data => {
          if (!data.gameId) return;
          setGame(data);
        });
    }
  });
  if (!game) return;
  return (
    <Grid
      templateColumns={{ base: 'repeat(1,1fr)', sm: 'repeat(3,1fr)' }}
      templateRow={{ base: 'repeat(3,1fr)', sm: 'repeat(1,1fr)' }}
      m={{ base: 5, md: 10 }}
      pt={10}
    >
      <GridItem>
        <GameDetails game={game} playerName={player} />
      </GridItem>
      <GridItem
        colStart={{ base: 0, sm: 2 }}
        rowStart={{ base: 2, sm: 1 }}
        mt={{ base: 5, sm: 0 }}
      >
        <GameArea game={game} player={player} />
      </GridItem>
    </Grid>
  );
};

export default Game;
