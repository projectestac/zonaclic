import React from 'react';
import { makeStyles } from "@material-ui/core/styles";
import { mergeClasses, htmlContent } from '../../utils/misc';
import SEO from '../SEO';
import ShareButtons from '../ShareButtons';

const useStyles = makeStyles(_theme => ({
  root: {
  },
}));

const shareSites = { moodle: true, classroom: true, embed: true };

function Project({ intl, project, SLUG, location, ...props }) {

  const {
    path, fullPath, meta_langs,
    title, author, school, date,
    languages, areas, levels, descriptors, description, license,
    relatedTo, mainFile, zipFile, instFile, files,
    cover, thumbnail,
    activities, mediaFiles, totalSize,
  } = project;
  const { locale, defaultLocale, messages } = intl;
  const classes = mergeClasses(props, useStyles());
  const k = meta_langs.includes(locale) ? locale : defaultLocale;
  const slug = `${SLUG}?act=${path}`
  const pageTitle = `${messages['repo-title']} - ${title}`;
  const pageDesc = description[k];
  const imgPath = cover && `${fullPath}/${cover}`;
  const moodleLink = `${fullPath}/${mainFile}`;
  const embedCode = `<iframe width="800" height="600" frameborder="0" allowFullScreen="true" src="${fullPath}/jclic.js/index.html"></iframe>`

  return (
    <div {...props} className={classes.root}>
      <SEO {...{ location, lang: locale, title: pageTitle, description: pageDesc, slug, thumbnail: imgPath }} />
      <h2>{title}</h2>
      <h3>{author}</h3>
      <h3>{school}</h3>
      <p>{date}</p>
      <ShareButtons {...{ shareSites, intl, link: location?.href, title, description, slug, thumbnail: imgPath || thumbnail, moodleLink, embedCode }} />
      {imgPath && <img src={imgPath} alt={messages['cover-alt']} />}
      <div dangerouslySetInnerHTML={{ __html: htmlContent(description[k]) }}></div>
    </div>
  );
}

export default Project;
