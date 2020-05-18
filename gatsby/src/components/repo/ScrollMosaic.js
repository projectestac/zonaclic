import React, { useState, useEffect } from 'react';
import { makeStyles } from "@material-ui/core/styles";
import { mergeClasses } from '../../utils/misc';
import InfiniteScroll from 'react-infinite-scroller';
import ProjectCard from './ProjectCard';

const useStyles = makeStyles(_theme => ({
  root: {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(14rem, 1fr))',
    gridGap: '1rem',
    "& a:link": {
      textDecoration: 'none',
    }
  },
}));

const blockSize = 30;

function ScrollMosaic({ intl, user, SLUG, repoBase, projects, ...props }) {

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
        <ProjectCard {...{ key: n, SLUG, user, messages, repoBase, project }} />
      ))}
    </InfiniteScroll>
  );
}

export default ScrollMosaic;