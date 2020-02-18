import React from 'react';
import { withPrefix } from 'gatsby';
import { Link } from 'gatsby-plugin-intl';
import { makeStyles } from "@material-ui/core/styles";
import Typography from '@material-ui/core/Typography';
import { mergeClasses, htmlContent } from '../../utils/misc';
import filesize from 'filesize';
import SEO from '../SEO';
import ShareButtons from '../ShareButtons';

const useStyles = makeStyles(_theme => ({
  root: {
  },
  cover: {
    maxWidth: '50%',
    float: 'right',
    marginTop: '1rem',
    marginLeft: '1rem',
    marginBottom: '1rem',
  },
  description: {
  },
  dataCard: {
    borderCollapse: 'collapse',
    minWidth: '80%',
    lineHeight: '100%',
    "& td": {
      border: 0,
    },
    "& td:first-child": {
      minWidth: '8rem',
      fontWeight: 'bold',
      paddingRight: '4pt',
      verticalAlign: 'top',
    },
  },
  cclogo: {
    float: 'left',
    marginRight: '0.9rem',
    opacity: .7,
  },
  related: {
    margin: 0,
    paddingLeft: 0,
    listStyleType: 'none'
  }
}));

const shareSites = { moodle: true, classroom: true, embed: true };

function Project({ intl, project, SLUG, REPO_BASE, location, ...props }) {

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
  const embedOptions = {
    width: '800',
    height: '600',
    frameborder: '0',
    allowFullScreen: 'true',
    src: `${fullPath}/jclic.js/index.html`,
  }

  return (
    <div {...props} className={classes.root}>
      <SEO {...{ location, lang: locale, title: pageTitle, description: pageDesc, slug, thumbnail: imgPath }} />
      <Typography variant="h3">{title}</Typography>
      <p>{author}</p>
      <ShareButtons {...{ shareSites, intl, link: location?.href, title, description, slug, thumbnail: imgPath || thumbnail, moodleLink, embedOptions }} />
      {imgPath && <img src={imgPath} alt={messages['cover-alt']} className={classes['cover']} />}
      <div className={classes['description']} dangerouslySetInnerHTML={{ __html: htmlContent(description[k]) }}></div>
      <br clear="all" />
      <table className={classes['dataCard']}>
        <tbody>
          <tr>
            <td>{`${messages['prj-authors']}:`}</td>
            <td>{author}</td>
          </tr>
          {school &&
            <tr>
              <td>{`${messages['prj-school']}:`}</td>
              <td>{school}</td>
            </tr>
          }
          {languages &&
            <tr>
              <td>{`${messages['prj-languages']}:`}</td>
              <td>{languages[locale] || languages[defaultLocale]}</td>
            </tr>
          }
          {areas &&
            <tr>
              <td>{`${messages['prj-subjects']}:`}</td>
              <td>{areas[locale] || areas[defaultLocale]}</td>
            </tr>
          }
          {levels &&
            <tr>
              <td>{`${messages['prj-levels']}:`}</td>
              <td>{levels[locale] || levels[defaultLocale]}</td>
            </tr>
          }
          {descriptors &&
            <tr>
              <td>{`${messages['prj-tags']}:`}</td>
              <td>{descriptors[locale] || descriptors[defaultLocale]}</td>
            </tr>
          }
          {date &&
            <tr>
              <td>{`${messages['prj-date']}:`}</td>
              <td>{date}</td>
            </tr>
          }
          {activities &&
            <tr>
              <td>{`${messages['prj-numacts']}:`}</td>
              <td>{activities}</td>
            </tr>
          }
          {totalSize &&
            <tr>
              <td>{`${messages['prj-size']}:`}</td>
              <td>{filesize(totalSize)}</td>
            </tr>
          }
          {license &&
            <tr>
              <td>{`${messages['prj-license']}:`}</td>
              <td>
                <img className={classes['cclogo']} src={withPrefix('/img/cclogo.png')} alt="CC" />
                <div dangerouslySetInnerHTML={{ __html: license[locale] || license[defaultLocale] }} />
              </td>
            </tr>
          }
          {relatedTo &&
            <tr>
              <td>{`${messages['prj-related']}:`}</td>
              <td>
                <ul className={classes['related']}>
                  {relatedTo.map((prj, n) => (
                    <li key={n}><Link to={`/repo/?act=${prj}`}>{prj}</Link></li>
                  ))}
                </ul>
              </td>
            </tr>
          }
        </tbody>
      </table>
    </div>
  );
}

export default Project;
