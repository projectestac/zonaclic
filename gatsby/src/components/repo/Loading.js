import React from 'react';
import { makeStyles } from "@material-ui/core/styles";
import { mergeClasses } from '../../utils/misc';
import Typography from "@material-ui/core/Typography";
import CircularProgress from '@material-ui/core/CircularProgress';

const useStyles = makeStyles(_theme => ({
  root: {
  },
  progress: {
    marginTop: '1rem',
  }
}));

function Loading({ intl, ...props }) {
  const classes = mergeClasses(props, useStyles());
  const { messages } = intl;
  return (
    <div className={classes.root}>
      <Typography variant="h3">{messages['repo-title']}</Typography>
      <p>{messages['loading']}</p>
      <CircularProgress className={classes['progress']} />
    </div>
  );
}

export default Loading;