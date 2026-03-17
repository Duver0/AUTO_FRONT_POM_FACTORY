# page-factory-basics

Create/maintain basic Page Object Model classes using Page Factory.

## Rules
- Base constructor receives `WebDriver`.
- Initialize page elements with `PageFactory.initElements`.
- Keep locators private with `@FindBy`.
- Expose only domain/business actions.

## Output
Reusable pages with clean separation from step definitions.
