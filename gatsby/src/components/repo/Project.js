import React, { useState } from 'react';
import { withPrefix } from 'gatsby';
import { Link } from 'gatsby-plugin-intl';
import { makeStyles } from "@material-ui/core/styles";
import { mergeClasses, htmlContent } from '../../utils/misc';
import ProjectDownload from './ProjectDownload';
import filesize from 'filesize';
import SEO from '../SEO';
import ShareButtons from '../ShareButtons';
import PlayIcon from '@material-ui/icons/PlayArrow';
import PlayCircleIcon from '@material-ui/icons/PlayCircleFilled';
import JavaIcon from '@material-ui/icons/LocalCafe';
import DownloadIcon from '@material-ui/icons/CloudDownload';
import IconButton from '@material-ui/core/IconButton';
import Button from '@material-ui/core/Button';

/*
const imgPlaceholder=`\
data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGIAAABiBAMAAABZixs9AAAAMFBMVE\
XZ2dnV1dW1tbW9vb2urq7c3NzQ0NDMzMzDw8O4uLjHx8fAwMCrq6uxsbGoqKjf39+WOJXRAAAAAW\
JLR0QAiAUdSAAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB+QDAgw7OygLvhcAAAG4SURBVF\
jDY2AYBaNgFIwCqoJQUuVYJYxxgzZsOsLmuOAGj1Kx6Ih+JYgb1Bhg0SGpgMcfzIpDXAdj+XIB0n\
RkLK9qJ0kHY1toqEQBKTq4ShkY2BpJ0SEF8sQyUnRkgYhlAaTYkUCqDq6lwCTaQFpYsYZKlJIYH+\
mkxQdDYNWqVII6wkSRhQNYCaerxWoBpKXEaEOh9aTpmM3Aqv2BFB25wBj4PIEEHVyKQE+wdgoQrY\
NxJzhoosyI1lHcCuEUFxCpg8scGrBsVlCGAH4djNYJMJ60KYSaHoBPB2vSAgR3ETghWglvxafjlz\
tSZIdfBHKYShma8OnYworMz01lYDwWwBB2LACnDqX5KHy26awpICOYGnDqOIWaAFmFy8HKWDcnEJ\
s/AjUhQRtlTqwOjo1QRnIDcToYd8ASV2BzqjAxOjgb4Ez26cFE6GC8hazbmLAO1rhSZG7PRII6wh\
6ghHU0YTtKUAoUBsI+55pIYq3G2CxAog5hQxJrTsbZJNa1rLINJOoIPBVAmg7WulQSWwBRbh+I0w\
FvLfUWEtlaCpsHbX0duUJki4xVYjdu0EadluUoGAWjYBSQDwD+eom39voA8AAAAABJRU5ErkJggg\
==`;
*/

const useStyles = makeStyles(theme => ({
  root: {
  },
  cover: {
    minWidth: '96px',
    minHeight: '96px',
    // backgroundImage: `url("${imgPlaceholder}")`,
    // backgroundSize: 'contain',
    // backgroundRepeat: 'no-repeat',
    maxWidth: '100%',
    maxHeight: '300px',
  },
  btnContainer: {
    display: 'inline-flex',
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: '1rem',
  },
  overlayBtn: {
    position: 'absolute',
    opacity: '20%',
    height: '10rem',
    width: '10rem',
    "& svg": {
      fontSize: 96,
    },
    "&:hover": {
      opacity: '90%',
    },
  },
  mainBlock: {
    marginTop: '1rem',
    marginBottom: '1rem',
  },
  description: {
    "& li": {
      marginBottom: '1rem',
    },
    [theme.breakpoints.up('sm')]: {
      width: '80%',
    },
  },
  dataCard: {
    borderCollapse: 'collapse',
    minWidth: '80%',
    marginTop: '1.5rem',
    marginBottom: '1.5rem',
    "& td": {
      border: 'none',
      borderBottom: '1px solid lightgray',
      borderTop: '1px solid lightgray',
      paddingLeft: 0,
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
  cctext: {
    fontSize: '9pt',
  },
  related: {
    margin: 0,
    paddingLeft: 0,
    listStyleType: 'none',
  },
  buttons: {
    display: 'flex',
    flexWrap: 'wrap',
    "& a,button": {
      marginRight: '1rem',
      marginBottom: '1rem',
    }
  },
}));

const shareSites = { moodle: true, classroom: true, embed: true };

function Project({ intl, project, SLUG, location, fullProjectList, jnlpInstaller, ...props }) {

  const {
    path, fullPath, meta_langs,
    title, author, school, date,
    languages, areas, levels, descriptors, description, license,
    relatedTo, mainFile, instFile,
    cover, thumbnail,
    activities, totalSize,
    // zipFile, files, mediaFiles,
  } = project;

  const { locale, defaultLocale, messages } = intl;
  const classes = mergeClasses(props, useStyles());
  const k = meta_langs.includes(locale) ? locale : defaultLocale;
  const slug = `${SLUG}?act=${path}`
  const pageTitle = `${messages['repo-title']} - ${title}`;
  const pageDesc = description[k];
  const imgPath = cover && `${fullPath}/${cover}`;
  const moodleLink = `${fullPath}/${mainFile}`;
  const projectLink = moodleLink.replace(/\/[^/]*$/, '/index.html');
  const instJavaLink = jnlpInstaller.replace('%%FILE%%', `${fullPath}/${instFile}`);
  const embedOptions = {
    width: '800',
    height: '600',
    frameborder: '0',
    allowFullScreen: 'true',
    src: projectLink,
  }
  const getProjectTitle = path => (fullProjectList && fullProjectList?.find(prj => prj.path === path)?.title) || path;

  const [dlgOpen, setDlgOpen] = useState(false);

  return (
    <div {...props} className={classes.root}>
      <SEO {...{ location, lang: locale, title: pageTitle, description: pageDesc, slug, thumbnail: imgPath }} />
      <h2>{title}</h2>
      <p>{author}</p>
      <ShareButtons {...{ shareSites, intl, link: location?.href, title, description, slug, thumbnail: imgPath || thumbnail, moodleLink, embedOptions }} />
      <div className={classes['mainBlock']}>
        {imgPath &&
          <div className={classes['btnContainer']}>
            <img src={imgPath} className={classes['cover']} alt={messages['cover-alt']} />
            <IconButton
              className={classes['overlayBtn']}
              color="primary"
              href={projectLink} target="_BLANK"
              title={messages['prj-launch-tooltip']}
            >
              <PlayIcon />
            </IconButton>
          </div>
        }
        <div className={classes['description']} dangerouslySetInnerHTML={{ __html: htmlContent(description[k]) }}></div>
      </div>
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
                <div className={classes['cctext']} dangerouslySetInnerHTML={{ __html: license[locale] || license[defaultLocale] }} />
              </td>
            </tr>
          }
          {relatedTo &&
            <tr>
              <td>{`${messages['prj-related']}:`}</td>
              <td>
                <ul className={classes['related']}>
                  {relatedTo.map((prj, n) => (
                    <li key={n}><Link to={`${SLUG}?act=${prj}`}>{getProjectTitle(prj)}</Link></li>
                  ))}
                </ul>
              </td>
            </tr>
          }
        </tbody>
      </table>
      <div className={classes['buttons']}>
        <Button
          variant="contained"
          color="primary"
          startIcon={<PlayCircleIcon />}
          href={projectLink}
          target="_BLANK"
          title={messages['prj-launch-tooltip']}
        >
          {messages['prj-launch']}
        </Button>
        <Button
          variant="contained"
          color="primary"
          startIcon={<DownloadIcon />}
          title={messages['prj-download-tooltip']}
          onClick={() => setDlgOpen(true)}
        >
          {messages['prj-download']}
        </Button>
        <Button
          variant="contained"
          color="primary"
          startIcon={<JavaIcon />}
          href={instJavaLink}
          target="_BLANK"
          title={messages['prj-java-inst-tooltip']}
        >
          {messages['prj-java-inst']}
        </Button>
      </div>
      <ProjectDownload {...{ intl, project, dlgOpen, setDlgOpen }} />
    </div>
  );
}

export default Project;
