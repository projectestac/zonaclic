import React from 'react';
import Typography from "@material-ui/core/Typography";
import { makeStyles } from "@material-ui/core/styles";
import { mergeClasses } from '../../utils/misc';
import Container from "@material-ui/core/Container";

const useStyles = makeStyles(theme => ({
  root: {
    color: 'green',
  },
}));

function RepoMain({ intl: { locale, messages }, ...props }) {

  const classes = mergeClasses(props, useStyles());

  return (
    <Container {...props} className={classes.root}>
      <Typography variant="h2">
        This is the repo!
      </Typography>
    </Container>
  );
}

export default RepoMain;