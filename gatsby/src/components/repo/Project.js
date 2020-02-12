import React from 'react';
import { makeStyles } from "@material-ui/core/styles";
import { mergeClasses, htmlContent } from '../../utils/misc';
import Container from "@material-ui/core/Container";
import SEO from '../SEO';
import { getAllVariants } from '../../utils/node';

const useStyles = makeStyles(_theme => ({
  root: {
  },
}));

function Project({ intl: { locale, defaultLocale, messages, formatMessage }, project, SLUG, location, ...props }) {

  const {
    path, fullPath, meta_langs,
    title, author, school, date,
    languages, areas, levels, descriptors, description, license,
    relatedTo, mainFile, zipFile, instFile, files,
    cover, thumbnail,
    activities, mediaFiles, totalSize,
  } = project;
  const classes = mergeClasses(props, useStyles());
  const k = meta_langs.includes(locale) ? locale : defaultLocale;
  const slug = `${SLUG}?act=${path}`
  const alt = getAllVariants(slug, location, locale);
  const pageTitle = `${messages['repo-title']} - ${title}`;
  const pageDesc = description[k];
  const imgPath = `${fullPath}/${cover}`;

  return (
    <>
      <SEO {...{ locale, title: pageTitle, description: pageDesc, slug, thumbnail: imgPath, alt }} />
      <Container {...props} className={classes.root}>
        <h2>{title}</h2>
        <h3>{`${author || ''}${school ? ` (${school})` : ''}`}</h3>
        <p>{date}</p>
        {cover && <img src={imgPath} alt={messages['cover-alt']} />}
        <div dangerouslySetInnerHTML={{ __html: htmlContent(description[k]) }}></div>
      </Container>
    </>
  );
}

export default Project;
