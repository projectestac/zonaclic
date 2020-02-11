import React from 'react';
import { makeStyles } from "@material-ui/core/styles";
import { mergeClasses } from '../../utils/misc';
import Container from "@material-ui/core/Container";

const useStyles = makeStyles(theme => ({
  root: {
  },
}));

function Project({ intl: { locale, defaultLocale, messages, formatMessage }, project, ...props }) {

  const {
    path, meta_langs,
    title, author, school, date,
    languages, areas, levels, descriptors, description, license,
    relatedTo, mainFile, zipFile, instFile, files,
    cover, thumbnail,
    activities, mediaFiles, totalSize,
  } = project;
  const classes = mergeClasses(props, useStyles());
  const k = meta_langs.includes(locale) ? locale : defaultLocale;

  return (
    <Container {...props} className={classes.root}>
      <h2>{title}</h2>
      <h3>{`${author || ''}${school ? ` (${school})` : ''}`}</h3>
      <p>{date}</p>
      {cover && <img src={`${path}/${cover}`} alt={messages['cover-alt']} />}
      <p>{description[k]}</p>
    </Container>
  );
}

export default Project;
