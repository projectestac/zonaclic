import React, { useState } from 'react';
import { makeStyles } from "@material-ui/core/styles";
import { mergeClasses } from '../../utils/misc';
import { Link } from 'gatsby-plugin-intl';
import Paper from '@material-ui/core/Card';
import Fab from '@material-ui/core/Fab';
import PlayIcon from '@material-ui/icons/PlayArrow';

const useStyles = makeStyles(theme => ({
  card: {
    maxWidth: '20rem',
  },
  cardContent: {
    position: 'relative',
    height: '10rem',
  },
  title: {
    position: 'absolute',
    bottom: 0,
    left: 0,
    right: 0,
    padding: '0.5rem 3rem 0.5rem 0.5rem',
    fontSize: '1rem',
    fontWeight: 'bold',
    color: theme.palette.grey[800],
    backgroundColor: 'rgba(255,255,255,0.85)',
  },
  language: {
    fontSize: '0.8rem',
    fontWeight: 'bold',
    padding: '0.15rem 0.15rem 0.3rem 0.1rem',
    margin: '0.4rem',
    display: 'inline-block',
    borderRadius: '4px',
    minWidth: '1.5rem',
    lineHeight: '1rem',
    textAlign: 'center',
    backgroundColor: theme.palette.info.dark,
    color: theme.palette.primary.contrastText,
  },
  playBtn: {
    position: 'absolute',
    right: 0,
    bottom: 0,
    margin: '0.4rem',
  },
  cardBottom: {
    whiteSpace: 'nowrap',
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    fontSize: '9pt',
    padding: '0.5rem',
  },
}));

function ProjectCard({ SLUG, user, messages, repoBase, project, children, ...props }) {

  const classes = mergeClasses(props, useStyles());
  const { path, title = 'Untitled', author = 'Unknown author', langCodes = [], mainFile, cover } = project;
  const base = `${repoBase}/${user ? `${user}/` : ''}${path}`;
  const projectLink = `${base}/${mainFile.replace(/[^/]*$/, 'index.html')}`;
  const [raised, setRaised] = useState(false);

  return (
    <Link to={`${SLUG}?${user ? `user=${user}&` : ''}act=${path}`} replace={false}>
      <Paper
        className={classes['card']}
        onMouseOver={() => setRaised(true)}
        onMouseOut={() => setRaised(false)}
        elevation={raised ? 8 : 1}
      >
        <div className={classes['cardContent']} style={{ background: `no-repeat center/150% url("${base}/${cover}")` }}>
          {langCodes.map(code => <span key={code} className={classes['language']}>{code}</span>)}
          <div className={classes['title']}>{title}</div>
          <Fab
            className={classes['playBtn']}
            color="primary"
            size="small"
            onClick={ev => {
              ev.preventDefault();
              window.open(projectLink, '_BLANK');
            }}
            title={messages['prj-launch-tooltip']}
          >
            <PlayIcon />
          </Fab>
        </div>
        <div className={classes['cardBottom']}>
          {children || author}
        </div>
      </Paper>
    </Link>
  );
}

export default ProjectCard;

