
// Default values

export const emptyPage = {
  body: '<p>unknown content</p>',
  frontmatter: {
    description: '',
    title: 'unknown content',
    date: new Date()
  },
};

export const emptyNode = { node: emptyPage };

export const dateFormat = {
  month: 'long',
  day: 'numeric',
  year: 'numeric'
};
