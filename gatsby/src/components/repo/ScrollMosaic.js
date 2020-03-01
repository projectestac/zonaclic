import React from 'react';
import { makeStyles } from "@material-ui/core/styles";
import { mergeClasses } from '../../utils/misc';
import Paper from '@material-ui/core/Paper';

const useStyles = makeStyles(_theme => ({
  root: {
  },
  card: {
    padding: '1rem',
  },
}));

function ScrollMosaic({ projects, ...props }) {

  const classes = mergeClasses(props, useStyles());

  return (
    <div {...props} className={classes.root}>
      {projects
        .slice(0, 30)
        .map(({ path, title, author, date, langCodes, levelCodes, mainFile, cover, thumbnail }, n) => (
          <Paper key={n} className={classes['card']}>
            {title}
          </Paper>
        ))
      }
    </div>
  );
}

export default ScrollMosaic;