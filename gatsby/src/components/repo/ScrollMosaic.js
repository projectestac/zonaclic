import React, { useState, useEffect } from 'react';
import { Link } from 'gatsby-plugin-intl';
import { makeStyles } from "@material-ui/core/styles";
import { mergeClasses } from '../../utils/misc';
import InfiniteScroll from 'react-infinite-scroller';
import Paper from '@material-ui/core/Card';
import Fab from '@material-ui/core/Fab';
import PlayIcon from '@material-ui/icons/PlayArrow';
import ArrowUp from '@material-ui/icons/ArrowUpward';

const useStyles = makeStyles(theme => ({
  root: {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(14rem, 1fr))',
    gridGap: '1rem',
    "& a:link": {
      textDecoration: 'none',
    }
  },
  toTop: {
    display: 'none',
    position: 'fixed',
    bottom: '1rem',
    right: '1rem',
  },
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
  author: {
    whiteSpace: 'nowrap',
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    fontSize: '9pt',
    padding: '0.5rem',
  },
}));


function ProjectCard({ SLUG, classes, messages, repoBase, project }) {

  const { path, title, author, langCodes, mainFile, cover } = project;
  const projectLink = `${repoBase}/${path}/${mainFile.replace(/\/[^/]*$/, '/index.html')}`;
  const [raised, setRaised] = useState(false);

  return (
    <Link to={`${SLUG}?act=${path}`} replace={false}>
      <Paper
        className={classes['card']}
        onMouseOver={() => setRaised(true)}
        onMouseOut={() => setRaised(false)}
        elevation={raised ? 8 : 1}
      >
        <div className={classes['cardContent']} style={{ background: `no-repeat center/150% url("${repoBase}/${path}/${cover}")` }}>
          {langCodes.map(code => <span key={code} className={classes['language']}>{code}</span>)}
          <div className={classes['title']}>{title}</div>
          <Fab
            className={classes['playBtn']}
            color="primary"
            size="small"
            onClick={() => window.open(projectLink, '_BLANK')}
            title={messages['prj-launch-tooltip']}
          >
            <PlayIcon />
          </Fab>
        </div>
        <div className={classes['author']}>
          {author}
        </div>
      </Paper>
    </Link>
  );
}

const blockSize = 30;

function ScrollMosaic({ intl, SLUG, repoBase, projects, topRef, ...props }) {

  const classes = mergeClasses(props, useStyles());
  const { messages } = intl;
  const [page, setPage] = useState(0);
  const [items, setItems] = useState(projects.slice(0, page * blockSize));

  const loadMore = () => {
    setPage(page + 1);
    setItems(projects.slice(0, page * blockSize));
  }

  useEffect(() => {
    setPage(0);
    loadMore();
  }, [projects]);

  return (
    <>
      <InfiniteScroll
        {...props}
        className={classes.root}
        pageStart={0}
        initialLoad={true}
        loadMore={loadMore}
        hasMore={projects.length > items.length}
        threshold={250}
        useWindow={true}
      >
        {items.map((project, n) => (
          <ProjectCard {...{ key: n, SLUG, classes, messages, repoBase, project }} />
        ))}
      </InfiniteScroll>
      <Fab
        className={classes['toTop']}
        title={messages['top']}
        onClick={() => {
          topRef.current.scrollIntoView({
            behavior: 'smooth',
            block: 'start',
          });
        }}
      >
        <ArrowUp />
      </Fab>
    </>
  );
}

export default ScrollMosaic;