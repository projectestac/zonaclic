module.exports = {
  globals: {
    __PATH_PREFIX__: true,
  },
  extends: `react-app`,
  rules: {
    "no-unused-vars": [1, { argsIgnorePattern: "^_", varsIgnorePattern: "^_" }],
  },
}